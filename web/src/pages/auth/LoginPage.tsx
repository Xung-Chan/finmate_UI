// src/pages/auth/LoginPage.tsx
import React from "react";
import { Button, Form, Input } from "antd";
import { useLogin } from "@/hooks/auth.hook";
import type { LoginRequest } from "@/types/auth.type";
// import { forgotPasswordRoute } from '@/routes/auth'

// import { loginRoute } from '@/routes/auth';
// import { useAuth } from "@/hooks/useAuth";
// import { ILoginRequestDto } from "@/types/auth.type";

const LoginPage: React.FC = () => {
    const [username, setUsername] = React.useState("");
    const [password, setPassword] = React.useState("");
    const { mutate: login, isPending } = useLogin();

    const handleSubmit = () => {
        const payload: LoginRequest = {
            username: username,
            password: password,
        };
        login(payload);
    }
    return (
        <>
            <div className="text-center mb-6">
                <h2 className="text-4xl font-bold text-gray-800">Đăng nhập</h2>
            </div>
            <Form layout="vertical" className="w-full max-w-sm">
                <Form.Item
                    label="username"
                    name="username"
                    rules={[{ required: true, message: "Username không được để trống!" }]}
                >
                    <Input
                        size="large"
                        placeholder="Nhập username"
                        onChange={(e) => setUsername(e.target.value)}
                        disabled={isPending}

                    />
                </Form.Item>
                <Form.Item
                    label="Mật khẩu"
                    name="password"
                    rules={[{ required: true, message: "Mật khẩu không được để trống!" }]}
                >
                    <Input.Password
                        size="large"
                        placeholder="Nhập mật khẩu"
                        onChange={(e) => setPassword(e.target.value)}
                        disabled={isPending}
                    />
                </Form.Item>

                <Form.Item>
                    <Button
                        loading={isPending}
                        className="mt-4 w-full bg-green-700 text-white hover:bg-green-800"
                        size="large"
                        type="primary"
                        onClick={handleSubmit}
                    >
                        Đăng nhập
                    </Button>
                </Form.Item>
            </Form>
        </>
    );
};

export default LoginPage;
