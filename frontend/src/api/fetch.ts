import {
  API_BASE_URL,
  LanguagesType,
  MessageType,
  SentenceType,
  WordsWithPartsOfSentenceType,
} from "./types";

const fetchFromApi = async <T>(path: string) => {
  const url = API_BASE_URL + path;
  try {
    const response = await fetch(url);

    if (!response.ok) {
      const errorMessage = await response.json();
      throw new Error(`Error: ${errorMessage.message}`);
    }

    return (await response.json()) as Promise<T>;
  } catch (error) {
    console.error("API call failed:", error);
    if (error instanceof SyntaxError) {
      throw new Error("The response body cannot be parsed as JSON");
    }

    if (error instanceof Error) {
      throw error;
    }
    throw new Error("An unknown error occurred.");
  }
};

export const fetchCSV = async () => {
  const url = API_BASE_URL + "/csv";

  try {
    const response = await fetch(url);

    if (!response.ok) {
      const errorMessage = await response.json();
      throw new Error(errorMessage.message);
    }

    const blob = await response.blob();
    const downloadUrl = window.URL.createObjectURL(blob);

    const a = document.createElement("a");
    a.href = downloadUrl;
    a.download = "flashcards.csv";

    document.body.appendChild(a);
    a.click();
    a.remove();

    window.URL.revokeObjectURL(downloadUrl);
  } catch (e) {
    if (e instanceof SyntaxError) {
      throw new Error("The response body cannot be parsed as JSON");
    }

    throw new Error((e as Error).message);
  }
};

export const fetchPDF = async () => {
  const url = API_BASE_URL + "/pdf";

  try {
    const response = await fetch(url);

    if (!response.ok) {
      const errorMessage = await response.json();
      throw new Error(errorMessage.message);
    }

    const blob = await response.blob();
    const downloadUrl = window.URL.createObjectURL(blob);

    const a = document.createElement("a");
    a.href = downloadUrl;
    a.download = "text.pdf";

    document.body.appendChild(a);
    a.click();
    a.remove();

    window.URL.revokeObjectURL(downloadUrl);
  } catch (e) {
    if (e instanceof SyntaxError) {
      throw new Error("The response body cannot be parsed as JSON");
    }

    throw new Error((e as Error).message);
  }
};

export const saveToDatabase = () => {
  return fetchFromApi<MessageType>("/deck/save");
};

export const fetchPartsOfSpeech = () => {
  return fetchFromApi<string[]>("/deck/parts_of_speech");
};

export const fetchDeck = () => {
  return fetchFromApi<SentenceType>("/deck");
};

export const fetchWordsWithPartsOfSentence = () => {
  return fetchFromApi<WordsWithPartsOfSentenceType>("/deck/sentences");
};

export const fetchLanguages = () => {
  return fetchFromApi<LanguagesType>("/deck/lang");
};
