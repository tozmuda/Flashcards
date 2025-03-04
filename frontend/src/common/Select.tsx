import { LanguagesType } from "../api/types";

type SelectProps = {
  onChange: (option: string) => void;
  options: string[] | LanguagesType;
  labelText?: string | null;
  value: string;
};

const Select = ({
  options,
  labelText = null,
  value,
  onChange,
}: SelectProps) => {
  return (
    <div>
      {labelText && <label className="text-gray-950">{labelText}</label>}
      <select
        onChange={(e) => onChange(e.currentTarget.value)}
        value={value}
        className="min-w-[200px] w-full border text-gray-50 bg-gray-950  border-gray-300 rounded-lg p-4 focus:ring-2 focus:ring-blue-500 focus:outline-none"
      >
        {options.map((o: string | LanguagesType[0]) => (
          <option
            key={typeof o === "string" ? o : o.languageShort}
            value={typeof o === "string" ? o : o.languageShort}
          >
            {!o ? "Select" : typeof o === "string" ? o : o.languageName}
          </option>
        ))}
      </select>
    </div>
  );
};

export default Select;
