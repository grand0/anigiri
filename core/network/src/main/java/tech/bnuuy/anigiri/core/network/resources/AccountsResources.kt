@file:Suppress("MatchingDeclarationName")

package tech.bnuuy.anigiri.core.network.resources

import io.ktor.resources.Resource

@Resource("/accounts")
class Accounts {
    
    @Resource("users")
    class Users(val parent: Accounts = Accounts()) {
        
        @Resource("auth")
        class Auth(val parent: Users = Users()) {
            
            @Resource("login")
            class Login(val parent: Auth = Auth())
            
            @Resource("logout")
            class Logout(val parent: Auth = Auth())
        }

        @Resource("me")
        class Me(val parent: Users = Users()) {

            @Resource("profile")
            class Profile(val parent: Me = Me())
            
            @Resource("favorites")
            class Favorites(val parent: Me = Me()) {
                
                @Resource("releases")
                class Releases(val parent: Favorites = Favorites())

                @Resource("ids")
                class Ids(val parent: Favorites = Favorites())
            }

            @Resource("collections")
            class Collections(val parent: Me = Me()) {

                @Resource("releases")
                class Releases(val parent: Collections = Collections())

                @Resource("ids")
                class Ids(val parent: Collections = Collections())
            }
        }
    }
}
