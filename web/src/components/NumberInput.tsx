import { Input, type InputProps } from "antd";
import { useEffect, useState } from "react";

interface NumberInputProps extends Omit<InputProps, "onChange"> {
  value?: string;
  onChange?: (value: string) => void;
}

const NumberInput = ({
  value,
  onChange,
  ...props
}: NumberInputProps) => {
  const [internal, setInternal] = useState(value ?? "");

  // sync khi dùng với AntD Form
  useEffect(() => {
    setInternal(value ?? "");
  }, [value]);

  return (
    <Input
      {...props}
      inputMode="numeric"
      value={internal}
      onChange={(e) => {
        const raw = e.target.value.replace(/\D/g, "");
        setInternal(raw);
        onChange?.(raw);
      }}
    />
  );
};

export default NumberInput;
