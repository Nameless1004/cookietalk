const TemporalError = ({ error, buttonMessage, handleError, ...props }) => {
  return (
    <div>
      <div>일시적인 오류가 발생했습니다.</div>
      <p>Error: {error}</p>
      <button onClick={handleError}>{buttonMessage}</button>
    </div>
  );
};

export default TemporalError;
