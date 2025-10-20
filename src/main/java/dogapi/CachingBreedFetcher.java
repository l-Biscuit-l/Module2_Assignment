package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private BreedFetcher fetcher;
    private Map<String,List<String>> cachemap = new HashMap<>();
    private int callsMade = 0;
    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) {
        if (cachemap.containsKey(breed)) {
            return cachemap.get(breed);
        }
        else {
                callsMade++;
                try {
                    List<String> breedlist = fetcher.getSubBreeds(breed);
                    cachemap.put(breed, breedlist);
                    return breedlist;
                } catch (BreedNotFoundException e) {
                    throw new BreedNotFoundException(breed);
                }
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}