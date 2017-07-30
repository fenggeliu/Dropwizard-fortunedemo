package fenggetest.core;


import java.util.*;

public class FortuneStore {

    private HashMap<String, String> map1;
    private HashMap<String, Integer> map2;
    private ArrayList<String> index;
    private Random rand;

    public FortuneStore(List<Fortune> fortunes) {
        HashMap<String, String> map1  = new HashMap<>();
        HashMap<String, Integer> map2  = new HashMap<>();
        ArrayList<String> index = new ArrayList<>();
        for (Fortune i : fortunes) {
            map1.put(i.getFortune(), i.getId());
            map2.put(i.getId(), index.size());
            index.add(i.getFortune());
        }
        this.map1 = map1;
        this.map2 = map2;
        this.index = index;
        this.rand = new Random();
    }


    public void insert(Fortune input) {
        String fortune = input.getFortune();
        String id = input.getId();
        if(map1.containsKey(fortune)){
            return;
        }

        map1.put(fortune, id);
        map2.put(id, index.size());
        index.add(fortune);
        return;
    }

    public void remove(String id) {
        if(map2.containsKey(id)){
            int removed = map2.get(id);

            map1.remove(index.get(removed));
            map2.remove(id);
            index.remove(removed);
            if(map1.size()==0){
                return;
            }

            if(removed ==map1.size()){
                return;
            }

            int size = index.size();
            String lastFortune = index.get(size - 1);
            String lastId = map1.get(lastFortune);
            Collections.swap(index, removed, size - 1);
            index.remove(size - 1);
            map2.put(lastId, removed);

        }else{
            return;
        }

        return;
    }

    public String getRandom() {
        if(map1.size()==0){
            return "No Fortune Yet";
        }

        if(map1.size()==1){
            return index.get(0);
        }

        return index.get(new Random().nextInt(index.size()));
        //return 0;
    }
}
