import { Input, type InputProps } from "antd";
import { useState } from "react";

interface NumberInputProps extends Omit<InputProps, "onChange"> {
  value?: number;
  onChange?: (value: number) => void;
}

const NumberInput = ({
  value,
  onChange,
  ...props
}: NumberInputProps) => {
  const [internal, setInternal] = useState(
    value !== undefined ? String(value) : ""
  );

  return (
    <Input
      {...props}
      inputMode="numeric"
      value={internal}
      onKeyDown={(e) => {
        const allowed = [
          "Backspace",
          "Delete",
          "ArrowLeft",
          "ArrowRight",
          "Tab",
        ];

        if (
          allowed.includes(e.key) ||
          (e.ctrlKey && ["a", "c", "v", "x"].includes(e.key.toLowerCase()))
        ) {
          return;
        }

        if (!/^\d$/.test(e.key)) {
          e.preventDefault();
        }
      }}
      onPaste={(e) => {
        const text = e.clipboardData.getData("text");
        if (!/^\d+$/.test(text)) {
          e.preventDefault();
        }
      }}
      onChange={(e) => {
        const raw = e.target.value.replace(/\D/g, "");
        setInternal(raw);
        onChange?.(raw ? Number(raw) : 0);
      }}
    />
  );
};

export default NumberInput;
