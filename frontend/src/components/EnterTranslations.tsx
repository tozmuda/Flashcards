import { FormEvent, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  FilledFlashcardType,
  WordsWithPartsOfSentenceType,
} from "../api/types";
import { postFlashcard } from "../api/post";
import { useGlobalError } from "../hooks/useGlobalError";
import Form from "../common/Form";
import Input from "../common/Input";
import FormButton from "../common/FormButton";
import Select from "../common/Select";
import { fetchWordsWithPartsOfSentence } from "../api/fetch";

type EnterTranslationsProps = {
  notFilledFlashcards: string[];
  partOfSpeechOptions: string[];
  setWordsWithPartsOfSentence: (words: WordsWithPartsOfSentenceType) => void;
};

const EnterTranslations = ({
  notFilledFlashcards,
  partOfSpeechOptions,
  setWordsWithPartsOfSentence,
}: EnterTranslationsProps) => {
  const [words, setWords] = useState<string[]>([]);

  const [partOfSpeech, setPartOfSpeech] = useState<string>(
    partOfSpeechOptions[0]
  );
  const [basicForm, setBasicForm] = useState<string>("");
  const [translation, setTranslation] = useState<string>("");
  const [transliteration, setTransliteration] = useState<string>("");
  const [transcription, setTranscription] = useState<string>("");

  useEffect(() => {
    setWords(notFilledFlashcards);
    setBasicForm(notFilledFlashcards.length > 0 ? notFilledFlashcards[0] : "");
  }, [notFilledFlashcards]);

  const navigate = useNavigate();
  const { setError } = useGlobalError();

  const handleGoToEnterPartOfSentence = async () => {
    try {
      const data: WordsWithPartsOfSentenceType =
        await fetchWordsWithPartsOfSentence();
      setWordsWithPartsOfSentence(data);

      setError("");

      navigate("/enter-part-of-sentence");
    } catch (e) {
      console.error(e);
      if (e instanceof Error) {
        setError(e.message);
      }
    }
  };

  const postText = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      const data: FilledFlashcardType = await postFlashcard({
        original: words[0],
        translation: translation,
        baseForm: basicForm,
        transliteration: transliteration,
        transcription: transcription,
        partOfSpeech: partOfSpeech,
      });
      console.log(`Added ${data.original} -> ${data.translation}`);

      setError("");

      if (words.length <= 1) {
        handleGoToEnterPartOfSentence();
      }

      setPartOfSpeech(partOfSpeechOptions[0]);
      setBasicForm(words.length > 1 ? words[1] : "");
      setTranslation("");
      setTransliteration("");
      setTranscription("");

      setWords((prevWords) => prevWords.slice(1));
    } catch (e) {
      console.error(e);
      if (e instanceof Error) {
        setError(e.message);
      }
    }
  };

  return (
    <Form onSubmit={(e) => postText(e)}>
      <h3 className="text-gray-950">Translations:</h3>
      <div className="flex flex-col gap-4">
        <div>
          <label className="text-gray-950">Word</label>
          <div className="min-w-[200px] w-full border text-gray-950 bg-gray-50 border-gray-300 rounded-lg p-4">
            {words[0]}
          </div>
        </div>
        <Select
          onChange={setPartOfSpeech}
          value={partOfSpeech}
          options={partOfSpeechOptions}
          labelText="Part of speech"
        />
        <Input
          onChange={setBasicForm}
          value={basicForm}
          placeholder="βίντεο"
          labelText="Basic form"
        />
        <Input
          onChange={setTranslation}
          value={translation}
          placeholder="film"
          labelText="Translation"
        />
        <Input
          onChange={setTranscription}
          value={transcription}
          placeholder="wideo"
          labelText="Transcription"
        />
        <Input
          onChange={setTransliteration}
          value={transliteration}
          placeholder="vínteo"
          labelText="Transliteration"
        />
      </div>
      <FormButton buttonText="Send" />
    </Form>
  );
};

export default EnterTranslations;
