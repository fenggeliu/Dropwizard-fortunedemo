package fenggetest.resources;

import views.Welcome;
import fenggetest.core.Fortune;
import fenggetest.core.FortuneStore;
import org.mongojack.DBQuery;
import org.mongojack.JacksonDBCollection;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class FortuneResource {

    private JacksonDBCollection<Fortune, String> collection;
    private FortuneStore fortuneStore;

    public FortuneResource(JacksonDBCollection<Fortune, String> collection, FortuneStore fortuneStore) {
        this.collection = collection;
        this.fortuneStore = fortuneStore;
    }

    @GET
    public Welcome welcome(){
        return new Welcome(fortuneStore.getRandom());
    }

    @Path("/fortune")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String tellFortune(){
        return fortuneStore.getRandom();
    }

    @Path("/fortunes")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public String addNewFortune(@Valid String fortune) {
        Fortune data = new Fortune(fortune);
        fortuneStore.insert(data);
        new Thread(() -> {
                collection.insert(data);
        }).start();

        return data.getId();
    }

    @Path("/fortunes/{id}")
    @DELETE
    public void removeFortuneById(@PathParam("id") String id) {
        fortuneStore.remove(id);
        new Thread(() -> {
            collection.findAndRemove(DBQuery.is("id", id));
        }).start();
    }

}
