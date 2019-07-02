//package com.sfy.demo.demo;
//
//import com.sfy.demo.dao.auth.Users;
//import com.sfy.demo.form.CreateUserForm;
//import com.sfy.demo.form.TUserInfo;
//import com.sfy.demo.form.UserTokenForm;
//import com.sfy.demo.utils.ApiResult;
//import com.sfy.demo.dto.TokenResult;
//import org.springframework.cloud.netflix.feign.FeignClient;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.cloud.netflix.feign.FeignClient;

//@FeignClient(name = "sfy-auth-v1", path = "sfy-auth-v1", url = "${feign-url}" ,fallback = IAuthServiceClient.FeignClientFallback.class)
//public interface IAuthServiceClient {
//    @RequestMapping(value = "/auth/createUser", method = RequestMethod.POST)
//    public ApiResult<TUserInfo> createUser(@RequestBody CreateUserForm createUserForm);
//
//    //TODO 获取用户扩展信息
//    @RequestMapping(value = "/auth/findUser", method = RequestMethod.GET)
//    public ApiResult<TUserInfo> findUserInfo(@RequestParam("phone") String phone);
//
//    //TODO 创建TOKEN
//    @RequestMapping(value = "/createTokenTOTokenResult", method = RequestMethod.POST)
//    public ApiResult<TokenResult> createTokenTOTokenResult(@RequestBody UserTokenForm userTokenForm);
//
//    //TODO 退出用户
//    @RequestMapping(value = "/auth/authExit", method = RequestMethod.GET)
//    public String authExit(@RequestParam("accessToken") String accessToken, @RequestParam("phone") String phone);
//
//    //TODO 获取用户信息
//    @RequestMapping(value = "/findUsersByUserNameAndPassword", method = RequestMethod.GET)
//    public ApiResult<Users> findUsersByUserNameAndPassword(@RequestParam("userName") String userName, @RequestParam("password") String password);
//
////    @Component
////    class FeignClientFallback implements IAuthServiceClient {
////
////        @Transactional
////        @Override
////        public ApiResult <TUserInfo> createUser(CreateUserForm createUserForm) {
////            System.out.println("aaaa");
////            return null;
////        }
////
////        @Override
////        public ApiResult <TUserInfo> findUserInfo(String phone) {
////            return null;
////        }
////
////        @Override
////        public ApiResult <TokenResult> createTokenTOTokenResult(UserTokenForm userTokenForm) {
////            return null;
////        }
////
////        @Override
////        public String authExit(String accessToken, String phone) {
////            return null;
////        }
////    }
//
//}
