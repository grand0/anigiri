@file:Suppress("MatchingDeclarationName")

package tech.bnuuy.anigiri.core.network.resources

import io.ktor.resources.Resource

@Resource("/anime")
class Anime {
    
    @Resource("releases")
    class Releases(val parent: Anime = Anime()) {
        
        @Resource("latest")
        class Latest(val parent: Releases = Releases())
        
        @Resource("random")
        class Random(val parent: Releases = Releases())
        
        @Resource("{id}")
        class Id(val parent: Releases = Releases(), val id: Int)
    }
    
    @Resource("catalog")
    class Catalog(val parent: Anime = Anime()) {
        
        @Resource("releases")
        class Releases(val parent: Catalog = Catalog())
        
        @Resource("references")
        class References(val parent: Catalog = Catalog()) {
            
            @Resource("genres")
            class Genres(val parent: References = References())
            
            @Resource("years")
            class Years(val parent: References = References())
        }
    }
}
