package xyz.lightningcapes.sources.manager;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.mongodb.client.MongoCollection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Getter
public class InGameNamesManager {

    private final MongoCollection<Document> collection;
    private final LoadingCache<Long, String> inGameCache = Caffeine.newBuilder()
            .expireAfterWrite(3, TimeUnit.MINUTES)
            .build(new CacheLoader<>() {
                @Override
                public @Nullable String load(Long id) {
                    Document document = collection.find(new Document("id", id)).first();
                    if (document == null)
                        return null;
                    return document.getString("name");
                }
            });

    public String getName(long id) {
        return this.inGameCache.get(id);
    }

    public boolean exists(String name) {
        return collection.find(new Document("name", name)).first() != null;
    }

    public void register(long id, String nick) {
        this.collection.insertOne(new Document("id", id).append("name", nick));
        this.inGameCache.asMap().put(id, nick);
    }
}