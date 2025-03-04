import { useEffect, useState } from "react";
import { WordsWithPartsOfSentenceType } from "../../api/types";
import PartOfSentenceModal from "./PartOfSentenceModal";

type DragProps = {
  simpleSentences: WordsWithPartsOfSentenceType[0]["simpleSentences"];
  setSimpleSentences: (
    sentenceIndex: number,
    elementIndex: number,
    values: string[]
  ) => void;
  setSimpleSentenceLevel: (
    sentenceIndex: number,
    elementIndex: number,
    level: number
  ) => void;
  sentenceIndex: number;
};

const Drag = ({
  simpleSentences,
  setSimpleSentences,
  sentenceIndex,
  setSimpleSentenceLevel,
}: DragProps) => {
  const initialElementPositions: WordsWithPartsOfSentenceType[0]["simpleSentences"][] =
    [
      simpleSentences.filter((s) => !s.level || s.level === 0),
      simpleSentences.filter((s) => s.level === 1),
      simpleSentences.filter((s) => s.level === 2),
      simpleSentences.filter((s) => s.level === 3),
    ];

  const [elementPositions, setElementPositions] = useState<
    WordsWithPartsOfSentenceType[0]["simpleSentences"][]
  >(initialElementPositions);

  useEffect(() => {
    setElementPositions([
      simpleSentences.filter((s) => !s.level || s.level === 0),
      simpleSentences.filter((s) => s.level === 1),
      simpleSentences.filter((s) => s.level === 2),
      simpleSentences.filter((s) => s.level === 3),
    ]);
  }, [simpleSentences]);

  const handleDragStart = (
    e: React.DragEvent<HTMLDivElement>,
    elementIndex: number
  ) => {
    if (isNaN(elementIndex)) return;
    e.dataTransfer.setData("elementIndex", elementIndex.toString());
  };

  const handleDrop = (
    e: React.DragEvent<HTMLDivElement>,
    targetBoxIndex: number
  ) => {
    const elementIndex = parseInt(e.dataTransfer.getData("elementIndex"));

    if (isNaN(elementIndex)) return;

    const elementToMove = simpleSentences[elementIndex];

    setSimpleSentenceLevel(sentenceIndex, elementIndex, targetBoxIndex);

    setElementPositions((prevPositions) => {
      const newPositions = prevPositions.map((box) =>
        box.filter((el) => el !== elementToMove)
      );

      newPositions[targetBoxIndex].push(elementToMove);

      return newPositions;
    });
  };

  const allowDrop = (e: React.DragEvent<HTMLDivElement>) => {
    e.preventDefault();
  };

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalContent, setModalContent] = useState<
    WordsWithPartsOfSentenceType[0]["simpleSentences"][0]
  >({ words: [] });
  const openModal = (
    content: WordsWithPartsOfSentenceType[0]["simpleSentences"][0],
    elementIndex: number
  ) => {
    setCurrentElementIndex(elementIndex);
    setModalContent(content);
    setIsModalOpen(true);
  };
  const [currentElementIndex, setCurrentElementIndex] = useState<number>(0);
  const closeModal = () => setIsModalOpen(false);
  const confirmModal = (elementIndex: number, values: string[]) => {
    setSimpleSentences(sentenceIndex, elementIndex, values);
    closeModal();
  };

  return (
    <div className="flex flex-col items-center gap-4 p-6 bg-gray-100 rounded-lg shadow-md">
      <div className="flex flex-col gap-1">
        {[0, 1, 2, 3].map((boxIndex) => (
          <div className="flex items-center gap-3 justify-between">
            <div className="text-gray-950 font-bold text-lg">
              {boxIndex + 1}
            </div>
            <div
              key={boxIndex}
              className="flex border-y-2 border-gray-950 bg-gray-600 flex-wrap"
              onDragOver={allowDrop}
              onDrop={(e) => handleDrop(e, boxIndex)}
            >
              {simpleSentences.map((element, elementIndex) => (
                <div
                  key={elementIndex}
                  className={`p-2 flex flex-wrap  ${
                    elementPositions[boxIndex].includes(element)
                      ? "bg-blue-300 text-gray-50 cursor-pointer"
                      : "invisible"
                  }`}
                  draggable={elementPositions[boxIndex].includes(element)}
                  onDragStart={(e) => handleDragStart(e, elementIndex)}
                  onDoubleClick={() => openModal(element, elementIndex)}
                >
                  {element.words.map((w, wordIndex) => (
                    <span
                      key={wordIndex}
                      className={`${
                        !w.possiblePartsOfSentence.length
                          ? "text-gray-950"
                          : w.selectedPartOfSentence
                          ? "ml-1 text-gray-950"
                          : "ml-1 text-red-500 font-bold"
                      }`}
                    >
                      {w.originalText}
                    </span>
                  ))}
                </div>
              ))}
            </div>
          </div>
        ))}

        <PartOfSentenceModal
          isOpen={isModalOpen}
          onClose={closeModal}
          onConfirm={confirmModal}
          modalContent={modalContent}
          elementIndex={currentElementIndex}
        />
      </div>
    </div>
  );
};

export default Drag;
