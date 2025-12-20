import { Input } from "antd";
import { SearchOutlined } from "@ant-design/icons";
import { useState } from "react";
import { colors } from "@/theme/color";

interface SearchComponentProps {
  placeholder?: string;
  value?: string;
  onChange?: (value: string) => void;
  onSearch?: (value: string) => void;
  width?: number;
}

export const SearchComponent: React.FC<SearchComponentProps> = ({
  placeholder = "Tìm kiếm...",
  value,
  onChange,
  onSearch,
  width = 250,
}) => {
  const [isFocused, setIsFocused] = useState(false);

  return (
    <Input
      prefix={<SearchOutlined style={{ color: colors.gray.g2 }} />}
      placeholder={placeholder}
      value={value}
      allowClear
      size="middle"
      onChange={(e) => onChange?.(e.target.value)}
      onPressEnter={(e) => onSearch?.((e.target as HTMLInputElement).value)}
      onFocus={() => setIsFocused(true)}
      onBlur={() => setIsFocused(false)}
      style={{
        width,
        height: 40,
        borderRadius: 6,
        border: `1.5px solid ${
          isFocused ? colors.blue.b2 : colors.gray.g1
        }`,
        boxShadow: isFocused
          ? `0 0 0 2px ${colors.blue.b2}22`
          : "none",
        transition: "all 0.25s ease",
      }}
    />
  );
};
