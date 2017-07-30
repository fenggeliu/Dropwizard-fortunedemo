package fenggetest;

import com.mongodb.DB;
import com.mongodb.Mongo;
import fenggetest.core.Fortune;
import fenggetest.core.FortuneStore;
import fenggetest.db.MongoManaged;
import fenggetest.health.MongoHealthCheck;
import fenggetest.resources.FortuneResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.mongojack.DBCursor;
import org.mongojack.JacksonDBCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FortuneServiceApplication extends Application<FortuneServiceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new FortuneServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "FortuneService";
    }

    @Override
    public void initialize(final Bootstrap<FortuneServiceConfiguration> bootstrap) {
        bootstrap.addBundle(new ViewBundle<FortuneServiceConfiguration>(){
            @Override
            public Map<String, Map<String, String>> getViewConfiguration(FortuneServiceConfiguration configuration){
                return configuration.getViewRendererConfiguration();
            }
        });
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run(final FortuneServiceConfiguration configuration,
                    final Environment environment) {
        Mongo mongo = new Mongo(configuration.mongohost, configuration.mongoport);
        MongoManaged mongoManaged = new MongoManaged(mongo);
        environment.lifecycle().manage(mongoManaged);
        environment.healthChecks().register("FortuneDb", new MongoHealthCheck(mongo));

        DB db = mongo.getDB(configuration.mongodb);
        JacksonDBCollection<Fortune, String> data = JacksonDBCollection.wrap(db.getCollection("FortuneService"), Fortune.class, String.class);
        DBCursor<Fortune> dbCursor = data.find();
        List<Fortune> fortunes = new ArrayList<>();
        while (dbCursor.hasNext()) {
            Fortune fortune = dbCursor.next();
            fortunes.add(fortune);
        }
        environment.jersey().register(new FortuneResource(data, new FortuneStore(fortunes)));
    }

}
