import Form from "../../common/Form";
import { WordsWithPartsOfSentenceType } from "../../api/types";
import FormButton from "../../common/FormButton";
import PlainButton from "../../common/PlainButton";
import Select from "../../common/Select";
import { useEffect, useState } from "react";

type PartOfSentenceModalProps = {
  isOpen: boolean;
  onClose: () => void;
  onConfirm: (elementIndex: number, values: string[]) => void;
  modalContent: WordsWithPartsOfSentenceType[0]["simpleSentences"][0];
  elementIndex: number;
};

const PartOfSentenceModal = ({
  isOpen,
  onClose,
  onConfirm,
  modalContent,
  elementIndex,
}: PartOfSentenceModalProps) => {
  const initialValues = modalContent.words
    .filter((w) => w.possiblePartsOfSentence.length)
    .map((w) => (w.selectedPartOfSentence ? w.selectedPartOfSentence : ""));

  useEffect(() => {
    setValues(
      modalContent.words
        .filter((w) => w.possiblePartsOfSentence.length)
        .map((w) => (w.selectedPartOfSentence ? w.selectedPartOfSentence : ""))
    );
  }, [modalContent]);

  const [values, setValues] = useState<string[]>(initialValues);

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-gray-800 bg-opacity-75 z-50">
      <Form
        onSubmit={(e) => {
          e.preventDefault();
          onConfirm(elementIndex, values);
        }}
      >
        <div className="p-2 flex flex-col gap-5">
          <h2 className="text-xl font-bold text-gray-950">
            Enter parts of sentence
          </h2>
          <div className="flex gap-3">
            {modalContent.words
              .filter((w) => w.possiblePartsOfSentence.length)
              .map((w, index) => (
                <div key={index} className="flex flex-col items-center gap-1">
                  <div className="text-gray-950">{w.originalText}</div>
                  <Select
                    onChange={(newValue) =>
                      setValues((prev) => {
                        const newValues = [...prev];
                        newValues[index] = newValue;
                        return newValues;
                      })
                    }
                    options={["", ...w.possiblePartsOfSentence]}
                    value={values[index]}
                  />
                </div>
              ))}
          </div>

          <div className="flex justify-end gap-3">
            <PlainButton onClick={onClose} buttonText="Cancel" />
            <FormButton buttonText="Save" />
          </div>
        </div>
      </Form>
    </div>
  );
};

export default PartOfSentenceModal;
