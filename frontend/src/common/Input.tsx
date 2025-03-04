type InputProps = {
  onChange: (text: string) => void;
  value: string;
  placeholder: string;
  labelText?: string | null;
};

const Input = ({
  onChange,
  value,
  placeholder,
  labelText = null,
}: InputProps) => {
  return (
    <div>
      {labelText && <label className="text-gray-950">{labelText}</label>}
      <input
        type="text"
        placeholder={placeholder}
        className="min-w-[200px] w-full border text-gray-50 bg-gray-950  border-gray-300 rounded-lg p-4 focus:ring-2 focus:ring-blue-500 focus:outline-none"
        onChange={(e) => onChange(e.currentTarget.value)}
        value={value}
      ></input>
    </div>
  );
};

export default Input;
