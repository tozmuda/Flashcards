import { createContext, useContext, useState } from "react";

type GlobalErrorContextType = {
  error: string;
  setError: (message: string) => void;
};

const GlobalErrorContext = createContext<GlobalErrorContextType | undefined>(
  undefined
);

export const GlobalErrorProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const [error, setError] = useState<string>("");

  return (
    <GlobalErrorContext.Provider value={{ error, setError }}>
      {children}
    </GlobalErrorContext.Provider>
  );
};

export const useGlobalError = (): GlobalErrorContextType => {
  const context = useContext(GlobalErrorContext);
  if (!context) {
    throw new Error("useGlobalError must be used within a GlobalErrorProvider");
  }
  return context;
};
