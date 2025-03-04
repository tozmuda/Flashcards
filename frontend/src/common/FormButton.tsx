type FormButtonProps = {
  buttonText: string;
};

const FormButton = ({ buttonText }: FormButtonProps) => {
  return (
    <button className="px-6 py-2 bg-blue-500 text-gray-50 font-semibold rounded-lg shadow hover:bg-blue-600 transition">
      {buttonText}
    </button>
  );
};

export default FormButton;
