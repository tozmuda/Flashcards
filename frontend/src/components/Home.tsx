import { FormEvent, MouseEvent, useCallback, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { postLanguage, postText } from "../api/post";
import {
  LanguagesType,
  MessageType,
  NotFilledFlashcardsType,
} from "../api/types";
import { deleteDeck } from "../api/delete";
import { useGlobalError } from "../hooks/useGlobalError";
import TextArea from "../common/TextArea";
import FormButton from "../common/FormButton";
import PlainButton from "../common/PlainButton";
import Form from "../common/Form";
import { fetchLanguages, fetchPartsOfSpeech } from "../api/fetch";
import Select from "../common/Select";

type HomeProps = {
  setNotFilledFlashcards: (flashcards: string[]) => void;
  setPartOfSpeechOptions: (options: string[]) => void;
};

const Home = ({
  setNotFilledFlashcards,
  setPartOfSpeechOptions,
}: HomeProps) => {
  const [text, setText] = useState<string>("");
  const [languages, setLanguages] = useState<LanguagesType>([]);
  const [languageValue, setLanguageValue] = useState<string>("PL");

  const navigate = useNavigate();
  const { setError } = useGlobalError();

  const handleFetchLanguages = useCallback(async () => {
    try {
      const languagesData: LanguagesType = await fetchLanguages();
      setLanguages(languagesData);
      setLanguageValue(languagesData[0].languageShort);

      setError("");
    } catch (e) {
      console.error(e);
      if (e instanceof Error) {
        setError(e.message);
      }
    }
  }, [setError]);

  useEffect(() => {
    handleFetchLanguages();
  }, [handleFetchLanguages]);

  const handlePostText = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      const deleteData: MessageType = await deleteDeck();
      console.log(deleteData.message);

      await postLanguage({
        languageShort: languageValue,
      });

      const data: NotFilledFlashcardsType = await postText({
        text: text,
      });
      setNotFilledFlashcards(data);

      const partsOfSpeechData: string[] = await fetchPartsOfSpeech();
      setPartOfSpeechOptions(partsOfSpeechData);

      setError("");

      navigate("/enter-translations");
    } catch (e) {
      console.error(e);
      if (e instanceof Error) {
        setError(e.message);
      }
    }
  };

  const handleGoToAddManually = async (e: MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();

    try {
      await postLanguage({
        languageShort: languageValue,
      });

      const partsOfSpeechData: string[] = await fetchPartsOfSpeech();
      setPartOfSpeechOptions(partsOfSpeechData);

      const deleteData: MessageType = await deleteDeck();
      console.log(deleteData.message);

      setError("");

      navigate("/final");
    } catch (e) {
      console.error(e);
      if (e instanceof Error) {
        setError(e.message);
      }
    }
  };

  const loremIpsum =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

  return (
    <Form onSubmit={(e) => handlePostText(e)}>
      <h3 className="text-gray-950">Select language</h3>
      <Select
        options={languages}
        value={languageValue}
        onChange={(value: string) => setLanguageValue(value)}
      />
      <h3 className="text-gray-950">Text:</h3>
      <TextArea placeholder={loremIpsum} text={text} onChange={setText} />
      <div className="flex gap-5">
        <FormButton buttonText="Send" />
        <PlainButton
          onClick={handleGoToAddManually}
          buttonText="Add manually"
        />
      </div>
    </Form>
  );
};
export default Home;
