import { MouseEvent } from "react";

type PlainButtonProps = {
  buttonText: string;
  onClick: (e: MouseEvent<HTMLButtonElement>) => void;
};

const PlainButton = ({ buttonText, onClick }: PlainButtonProps) => {
  return (
    <button
      type="button"
      onClick={(e) => onClick(e)}
      className="px-6 py-2 bg-gray-50 text-blue-500 border border-blue-500 font-semibold rounded-lg shadow hover:bg-blue-100 transition"
    >
      {buttonText}
    </button>
  );
};

export default PlainButton;
