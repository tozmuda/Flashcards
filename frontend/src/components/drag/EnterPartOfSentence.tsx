import { MouseEvent, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  FilledWordWithPartOfSentenceType,
  MessageType,
  WordsWithPartsOfSentenceType,
} from "../../api/types";
import { postFilledWordWithPartOfSentence } from "../../api/post";
import { useGlobalError } from "../../hooks/useGlobalError";
import Drag from "./Drag";
import PlainButton from "../../common/PlainButton";

type EnterPartOfSentenceProps = {
  wordsWithPartsOfSentence: WordsWithPartsOfSentenceType;
};

const EnterPartOfSentence = ({
  wordsWithPartsOfSentence,
}: EnterPartOfSentenceProps) => {
  const [words, setWords] = useState<WordsWithPartsOfSentenceType>([]);

  useEffect(() => {
    setWords(wordsWithPartsOfSentence);
  }, [wordsWithPartsOfSentence]);

  useEffect(() => {
    const modifiedWords = wordsWithPartsOfSentence.map((sentence) => ({
      ...sentence,
      simpleSentences: sentence.simpleSentences.map((simpleSentence) => ({
        ...simpleSentence,
        level: 0,
        words: simpleSentence.words.map((w) => ({
          ...w,
          selectedPartOfSentence:
            w.possiblePartsOfSentence.length === 1
              ? w.possiblePartsOfSentence[0]
              : w.selectedPartOfSentence, // Keep the existing value if not auto-selecting
        })),
      })),
    }));

    setWords(modifiedWords);
  }, [wordsWithPartsOfSentence]);

  const navigate = useNavigate();
  const { setError } = useGlobalError();

  const handleConfirm = async (e: MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();

    for (const simpleSentence of words) {
      for (const s of simpleSentence.simpleSentences) {
        for (const w of s.words) {
          if (!w.selectedPartOfSentence && w.possiblePartsOfSentence.length) {
            setError("Some words' parts of sentences are not selected");
            return;
          }
        }
      }
    }

    const preparedData: FilledWordWithPartOfSentenceType = words.map(
      (simpleSentence) =>
        simpleSentence.simpleSentences.map((s) => ({
          level: !s.level ? 0 : s.level,
          selectedPartsOfSentence: s.words.map((w) =>
            !w.selectedPartOfSentence ? null : w.selectedPartOfSentence
          ),
        }))
    );

    try {
      const data: MessageType = await postFilledWordWithPartOfSentence(
        preparedData
      );
      console.log(data);

      setError("");

      navigate("/final");
    } catch (e) {
      console.error(e);
      if (e instanceof Error) {
        setError(e.message);
      }
    }
  };

  const setSimpleSentenceLevel = (
    sentenceIndex: number,
    elementIndex: number,
    level: number
  ) => {
    setWords((prev) => {
      const newWords = structuredClone(prev);

      newWords[sentenceIndex].simpleSentences[elementIndex].level = level;

      return newWords;
    });
  };

  const setSimpleSentences = (
    sentenceIndex: number,
    elementIndex: number,
    values: string[]
  ) => {
    setWords((prev) => {
      const newWords = structuredClone(prev);

      const wordsArray =
        newWords[sentenceIndex].simpleSentences[elementIndex].words;

      const modifiedWordsArray = wordsArray.map((w) =>
        !w.possiblePartsOfSentence.length
          ? w
          : { ...w, selectedPartOfSentence: values.shift() }
      );

      newWords[sentenceIndex].simpleSentences[elementIndex].words =
        modifiedWordsArray;

      return newWords;
    });
  };

  return (
    <div className="flex flex-col gap-10 items-center">
      <div className="flex flex-col items-center gap-4 p-6 bg-gray-100 rounded-lg shadow-md text-gray-950 text-center">
        Double click on element to edit its parts of sentence
        <br />
        You can drag elements between boxes to determine the syntax of complex
        sentences
      </div>
      {words.map((w, index) => (
        <div key={index} className="px-5 w-screen">
          <Drag
            key={index}
            simpleSentences={w.simpleSentences}
            setSimpleSentences={setSimpleSentences}
            sentenceIndex={index}
            setSimpleSentenceLevel={setSimpleSentenceLevel}
          />
        </div>
      ))}
      <PlainButton onClick={handleConfirm} buttonText="Confirm" />
    </div>
  );
};

export default EnterPartOfSentence;
