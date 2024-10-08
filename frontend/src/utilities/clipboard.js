export const handleClipboard = () => {
  const handleCopyClipboard = async (text, message) => {
    try {
      await navigator.clipboard.writeText(text);
      alert(message ?? '클립보드에 복사되었습니다.');
    } catch (error) {
      alert('복사 실패. 다시 시도해주세요!');
      console.error(error);
    }
  };

  return { handleCopyClipboard };
};
