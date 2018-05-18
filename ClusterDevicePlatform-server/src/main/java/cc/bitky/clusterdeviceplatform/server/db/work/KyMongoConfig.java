package cc.bitky.clusterdeviceplatform.server.db.work;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.reactivestreams.client.MongoClients;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

import cc.bitky.clusterdeviceplatform.server.config.DbSetting;

@Configuration
public class KyMongoConfig extends AbstractMongoConfiguration {

    @Bean
    public com.mongodb.reactivestreams.client.MongoClient reactiveMongoClient() {
        if (DbSetting.AUTHENTICATION_STATUS) {
            return MongoClients.create("mongodb://" + DbSetting.DATABASE_USERNAME + ":" + DbSetting.DATABASE_PASSWORD + "@" + DbSetting.MONGODB_HOST + ":" + DbSetting.MONGODB_PORT + "/" + DbSetting.DATABASE);
        } else {
            return MongoClients.create("mongodb://" + DbSetting.MONGODB_HOST + ":" + DbSetting.MONGODB_PORT);
        }
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(reactiveMongoClient(), getDatabaseName());
    }

    @Override
    public MongoClient mongoClient() {
        if (DbSetting.AUTHENTICATION_STATUS) {
            return new MongoClient(
                    new ServerAddress(DbSetting.MONGODB_HOST, DbSetting.MONGODB_PORT),
                    MongoCredential.createCredential(DbSetting.DATABASE_USERNAME, getDatabaseName(), DbSetting.DATABASE_PASSWORD.toCharArray()),
                    MongoClientOptions.builder().build()
            );
        } else {
            return new MongoClient(new ServerAddress(DbSetting.MONGODB_HOST, DbSetting.MONGODB_PORT));
        }
    }

    @Override
    protected String getDatabaseName() {
        return DbSetting.DATABASE;
    }

    @Override
    public MappingMongoConverter mappingMongoConverter() throws Exception {
        MappingMongoConverter mmc = super.mappingMongoConverter();
        //remove _class
        mmc.setTypeMapper(new DefaultMongoTypeMapper(null));
        return mmc;
    }
}
