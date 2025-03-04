import { FlashcardTypeExtended, SentenceType } from "../../../api/types";

export const generateFullSentence = (fullSentence: SentenceType[0]) => {
  return fullSentence.map((s, index) => (
    <div
      className={
        index === fullSentence.length - 1 ? "" : "border-b-4 border-gray-800"
      }
    >
      {generateSimpleSentence(s.words, s.level)}
    </div>
  ));
};

const generateSimpleSentence = (
  words: FlashcardTypeExtended[],
  level: number
) => {
  const multiplier = 80;

  return (
    <div className="flex-wrap flex">
      <div
        className="flex flex-col h-[193px] px-6 py-2"
        style={{ paddingRight: level * multiplier }}
      >
        <div className="font-extrabold text-red-500 flex-1">Original word:</div>
        <div className="text-blue-500 font-extrabold flex-1">
          Part of speech:
        </div>
        <div className="text-blue-600 font-extrabold flex-1">
          Part of sentence:
        </div>
        <div className="text-green-500 font-extrabold flex-1">Base form:</div>
        <div className="text-gray-950 font-extrabold flex-1">Translation:</div>
        <div className="text-yellow-900 font-extrabold flex-1">
          Transcription:
        </div>
        <div className="text-yellow-800 font-extrabold flex-1">
          Transliteration:
        </div>
      </div>
      {words.map((w, index) => (
        <div key={index} className="flex flex-col h-[193px] px-6 py-2">
          <div className="underline text-red-500 font-semibold flex-grow flex-1">
            {w.original}
          </div>
          <div className="text-blue-500 flex-1">{w.partOfSpeech}</div>
          <div className="text-blue-600 flex-1">{w.partOfSentence}</div>
          <div className="text-green-500 underline font-semibold flex-1">
            {w.baseForm}
          </div>
          <div className="underline text-gray-950 font-semibold flex-1">
            {w.translation}
          </div>
          <div className="text-yellow-900  flex-1">{w.transcription}</div>
          <div className="text-yellow-800 flex-1">{w.transliteration}</div>
        </div>
      ))}
    </div>
  );
};
