import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import App from "./App.tsx";
import { GlobalErrorProvider } from "./hooks/useGlobalError.tsx";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <GlobalErrorProvider>
      <App />
    </GlobalErrorProvider>
  </StrictMode>
);
