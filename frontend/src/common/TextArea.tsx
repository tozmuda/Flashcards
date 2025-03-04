type TextAreaProps = {
  placeholder: string;
  onChange: (text: string) => void;
  text: string;
};

const TextArea = ({ placeholder, onChange, text }: TextAreaProps) => {
  return (
    <textarea
      wrap="soft"
      placeholder={placeholder}
      className="min-w-[500px] text-gray-50 bg-gray-950 w-full border border-gray-300 rounded-lg p-4 focus:ring-2 focus:ring-blue-500 focus:outline-none"
      rows={8}
      onChange={(e) => onChange(e.currentTarget.value)}
      value={text}
    ></textarea>
  );
};

export default TextArea;
