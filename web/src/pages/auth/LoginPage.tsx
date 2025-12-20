// src/pages/auth/LoginPage.tsx
import React from "react";
import { useNavigate } from "@tanstack/react-router";
import { Button, Form, Input } from "antd";
import { Link } from '@tanstack/react-router'
// import { forgotPasswordRoute } from '@/routes/auth'

// import { loginRoute } from '@/routes/auth';
// import { useAuth } from "@/hooks/useAuth";
// import { ILoginRequestDto } from "@/types/auth.type";

const LoginPage: React.FC = () => {
    // const { useLogin } = useAuth();
    // const { mutate, status } = useLogin();
    // const { title } = loginRoute.useLoaderData();

    // const onFinish = (values: ILoginRequestDto) => {
    //     mutate(values);
    // };
    const navigate = useNavigate();
    return (
        <>
            <div className="text-center mb-6">
                <h2 className="text-4xl font-bold text-gray-800">Đăng nhập</h2>
            </div>
            <Form layout="vertical" className="w-full max-w-sm">
                <Form.Item
                    label="Email"
                    name="email"
                    rules={[{ required: true, message: "Email không được để trống!" }]}
                >
                    <Input size="large" placeholder="Nhập Email" />
                </Form.Item>
                <Form.Item
                    label="Mật khẩu"
                    name="password"
                    rules={[{ required: true, message: "Mật khẩu không được để trống!" }]}
                >
                    <Input.Password size="large" placeholder="Nhập mật khẩu" />
                </Form.Item>

                <Form.Item>
                    <Button
                        loading={status === "pending"}
                        className="mt-4 w-full bg-green-700 text-white hover:bg-green-800"
                        size="large"
                        type="primary"
                        htmlType="submit"
                        onClick={() => navigate({ to: '/manage' })}
                    >
                        Đăng nhập
                    </Button>
                </Form.Item>

                <div className="flex justify-center">
                    {/* <Link to={forgotPasswordRoute.to} className="text-gray-600 hover:text-green-700 hover:underline">
                        Quên mật khẩu?
                    </Link> */}
                    <Link to={"/forgot-password"} className="text-gray-600 hover:text-green-700 hover:underline">
                        Quên mật khẩu?
                    </Link>
                </div>
            </Form>
        </>
    );
};

export default LoginPage;
