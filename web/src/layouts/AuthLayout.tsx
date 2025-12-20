import React from "react";
import { Outlet } from "@tanstack/react-router";
import loginImage from "@/assets/bg_login.png";
import logo from "@/assets/Logomark.png";

const AuthLayout: React.FC = () => {
  return (
    <div className="flex w-screen h-screen overflow-hidden bg-white">
      {/* Cột trái – ảnh nền full */}
      <div className="w-[53%] h-full relative overflow-hidden">
        <img
          src={loginImage}
          alt="Background"
          className="absolute w-full h-full object-cover"
        />
      </div>

      {/* Cột phải – form đăng nhập */}
      <div className="w-[47%] h-full bg-white flex flex-col justify-center items-center px-12">
        
        <img src={logo} alt="logo" className="h-20 mb-6" />
        <Outlet />
      </div>
    </div>
  );
};

export default AuthLayout;
