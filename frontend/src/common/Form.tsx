import { ReactNode } from "react";

type FormProps = {
  children: ReactNode;
  onSubmit: (e: React.FormEvent<HTMLFormElement>) => void;
};

const Form = ({ children, onSubmit }: FormProps) => {
  return (
    <form
      role="form"
      onSubmit={(e) => onSubmit(e)}
      className="flex flex-col items-center gap-4 p-6 bg-gray-100 rounded-lg shadow-md"
    >
      {children}
    </form>
  );
};

export default Form;
