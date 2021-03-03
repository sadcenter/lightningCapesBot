package xyz.lightningcapes.sources.manager;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.TimeUnit;

public final class InGameNamesManager {

    private final MongoCollection<Document> collection;
    private final LoadingCache<Long, String> inGameCache = Caffeine.newBuilder().expireAfterWrite(2, TimeUnit.HOURS).build(new CacheLoader<Long, String>() {
        @Override
        public @Nullable String load(Long id) throws Exception {
            Document document = collection.find(new Document("id", id)).first();

            if (document == null)
                return null;

            return document.getString("name");

        }
    });


    public InGameNamesManager(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    public String getName(long id) {
        return this.inGameCache.get(id);
    }

    public boolean exists(String name) {
        return collection.find(new Document("name", name)).first() != null;
    }

    public void register(long id, String nick) {
        this.collection.insertOne(new Document("id", id).append("name", nick));
        this.inGameCache.put(id, nick);
    }

    public LoadingCache<Long, String> getRaw() {
        return inGameCache;
    }
}
