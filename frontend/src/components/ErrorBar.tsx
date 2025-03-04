import { useGlobalError } from "../hooks/useGlobalError";

const ErrorBar = () => {
  const { error } = useGlobalError();

  return (
    <>
      {error && (
        <div className="mt-2 border-4 border-red-500 bg-gray-50 text-red-500 font-bold rounded-lg p-4">
          {error}
        </div>
      )}
    </>
  );
};

export default ErrorBar;
