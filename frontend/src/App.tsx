import { MemoryRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./components/Home";
import EnterTranslations from "./components/EnterTranslations";
import { useState } from "react";
import ErrorBar from "./components/ErrorBar";
import { WordsWithPartsOfSentenceType } from "./api/types";
import Final from "./components/final/Final";
import EnterPartOfSentence from "./components/drag/EnterPartOfSentence";

function App() {
  const [notFilledFlashcards, setNotFilledFlashcards] = useState<string[]>([]);
  const [wordsWithPartsOfSentence, setWordsWithPartsOfSentence] =
    useState<WordsWithPartsOfSentenceType>([]);
  const [partOfSpeechOptions, setPartOfSpeechOptions] = useState<string[]>([
    "error",
  ]);

  return (
    <Router>
      <div className="flex flex-col items-center h-screen">
        <h1 className="text-3xl font-mono my-3 font-bold text-gray-50">
          FLASHCARD CREATOR
        </h1>
        <div className="flex justify-center w-full p-5">
          <Routes>
            <Route
              path="/"
              element={
                <Home
                  setNotFilledFlashcards={setNotFilledFlashcards}
                  setPartOfSpeechOptions={setPartOfSpeechOptions}
                />
              }
            />
            <Route
              path="/enter-translations"
              element={
                <EnterTranslations
                  notFilledFlashcards={notFilledFlashcards}
                  partOfSpeechOptions={partOfSpeechOptions}
                  setWordsWithPartsOfSentence={setWordsWithPartsOfSentence}
                />
              }
            />
            <Route
              path="/enter-part-of-sentence"
              element={
                <EnterPartOfSentence
                  wordsWithPartsOfSentence={wordsWithPartsOfSentence}
                />
              }
            />
            <Route
              path="/final"
              element={<Final partOfSpeechOptions={partOfSpeechOptions} />}
            />
          </Routes>
        </div>
        <ErrorBar />
      </div>
    </Router>
  );
}

export default App;
