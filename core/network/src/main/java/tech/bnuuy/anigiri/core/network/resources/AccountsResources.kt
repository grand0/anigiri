package tech.bnuuy.anigiri.core.network.resources

import io.ktor.resources.Resource

@Resource("/accounts")
class Accounts() {
    
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
        }
    }
}
