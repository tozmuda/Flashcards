import { FormEvent, MouseEvent, useState } from "react";
import { FilledFlashcardType, MessageType } from "../../../api/types";
import { postFlashcard } from "../../../api/post";
import { fetchCSV, fetchPDF, saveToDatabase } from "../../../api/fetch";
import { useGlobalError } from "../../../hooks/useGlobalError";
import { useNavigate } from "react-router-dom";
import Form from "../../../common/Form";
import Input from "../../../common/Input";
import FormButton from "../../../common/FormButton";
import PlainButton from "../../../common/PlainButton";
import Select from "../../../common/Select";

type AddManuallyProps = {
  partOfSpeechOptions: string[];
};

const AddManually = ({ partOfSpeechOptions }: AddManuallyProps) => {
  const [basicForm, setBasicForm] = useState<string>("");
  const [partOfSpeech, setPartOfSpeech] = useState<string>("");
  const [translation, setTranslation] = useState<string>("");
  const [transliteration, setTransliteration] = useState<string>("");
  const [transcription, setTranscription] = useState<string>("");

  const navigate = useNavigate();
  const { setError } = useGlobalError();
  const [info, setInfo] = useState<string>("");

  const handlePostText = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      const data: FilledFlashcardType = await postFlashcard({
        original: basicForm,
        translation: translation,
        baseForm: basicForm,
        transliteration: transliteration,
        transcription: transcription,
        partOfSpeech: partOfSpeech,
      });

      setError("");
      console.log(`Added ${data.original} -> ${data.translation}`);
      setPartOfSpeech("");
      setBasicForm("");
      setTranslation("");
      setTransliteration("");
      setTranscription("");
    } catch (e) {
      console.error(e);
      if (e instanceof Error) {
        setError(e.message);
      }
    }
  };

  const handleFetchCSV = async (e: MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();

    try {
      await fetchCSV();
      setError("");
    } catch (e) {
      console.error(e);
      if (e instanceof Error) {
        setError(e.message);
      }
    }
  };

  const handleFetchPDF = async (e: MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();

    try {
      await fetchPDF();
      setError("");
    } catch (e) {
      console.error(e);
      if (e instanceof Error) {
        setError(e.message);
      }
    }
  };

  const handleClearAllFlashcards = (e: MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    navigate("/");
  };

  const handleSaveToDatabase = async (e: MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    try {
      const data: MessageType = await saveToDatabase();
      setInfo(data.message);
      setError("");
    } catch (e) {
      console.error(e);
      if (e instanceof Error) {
        setError(e.message);
      }
    }
  };

  return (
    <Form onSubmit={(e) => handlePostText(e)}>
      <h3 className="text-gray-950">Add words manually:</h3>
      <div className="flex flex-col gap-4">
        <Input
          onChange={setBasicForm}
          value={basicForm}
          placeholder="βίντεο"
          labelText="Basic form"
        />
        <Select
          onChange={setPartOfSpeech}
          value={partOfSpeech}
          options={partOfSpeechOptions}
          labelText="Part of speech"
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
      <div className="flex gap-5">
        <PlainButton buttonText="Download CSV" onClick={handleFetchCSV} />
        <PlainButton buttonText="Download PDF" onClick={handleFetchPDF} />
        <PlainButton
          buttonText="Clear all flashcards"
          onClick={handleClearAllFlashcards}
        />
        <PlainButton
          buttonText="Save to database"
          onClick={handleSaveToDatabase}
        />
      </div>
      {info && (
        <div className="bg-blue-200 border-2 border-blue-500 text-gray-950 rounded-full px-8 py-3">
          {info}
        </div>
      )}
    </Form>
  );
};

export default AddManually;
