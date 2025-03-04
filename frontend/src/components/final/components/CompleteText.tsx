import { useEffect, useState } from "react";
import { SentenceType } from "../../../api/types";
import { fetchDeck } from "../../../api/fetch";
import { useGlobalError } from "../../../hooks/useGlobalError";
import { generateFullSentence } from "./completeTextHelpers";

const CompleteText = () => {
  const [completeText, setCompleteText] = useState<SentenceType>([]);
  const { setError } = useGlobalError();

  useEffect(() => {
    const handleFetchDeck = async () => {
      try {
        const data: SentenceType = await fetchDeck();
        setError("");
        setCompleteText(data);
      } catch (e) {
        console.error(e);
        if (e instanceof Error) {
          setError(e.message);
        }
      }
    };

    handleFetchDeck();
  }, [setError]);

  return (
    <>
      {completeText.length ? (
        completeText.map((c, index) => (
          <div
            key={index}
            className="mt-3 p-6 bg-gray-100 rounded-lg shadow-md "
          >
            {generateFullSentence(c)}
          </div>
        ))
      ) : (
        <></>
      )}
    </>
  );
};

export default CompleteText;
