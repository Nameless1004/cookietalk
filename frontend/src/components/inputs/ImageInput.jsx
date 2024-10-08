import { useEffect, useRef, useState } from 'react';
import Button from './Button.jsx';

const urlToFile = async (url) => {
  if (!url) return null;
  const response = await fetch(url);
  const blob = await response.blob();
  return new File([blob], 'thumbnail', { type: blob.type });
};

const ImageInput = ({ value, setValue, originUrl, buttonLabel }) => {
  const inputRef = useRef(null);
  const handleInputClick = () => {
    inputRef.current?.click();
  };

  const [previewUrl, setPreviewUrl] = useState('');
  const handleImgInput = (e) => {
    const file = e.target.files[0];
    setValue(file);
    createPreviewUrl(file);
  };

  const createPreviewUrl = (file) => {
    if (previewUrl) URL.revokeObjectURL(previewUrl);
    const url = URL.createObjectURL(file);
    setPreviewUrl(url);
  };

  useEffect(() => {
    let isMounted = true;

    (async () => {
      const prevThumbnailFile = await urlToFile(originUrl);

      if (isMounted && prevThumbnailFile) {
        setValue(prevThumbnailFile);
        createPreviewUrl(prevThumbnailFile);
        // 원래 이전 thumbnail이랑 비교하기 위한 setPrevThumbnail()해두고 form 제출할 때 이전/현재 비교했는데 이 부분 어떻게하면 좋을지 찾아보기
      }
    })();

    return () => {
      isMounted = false;
    };
  }, []);

  return (
    <div className='flex flex-col justify-center items-center gap-3'>
      <div
        id='thumbnailRegion'
        className='rounded-full -bg--light-gray-0 w-[120px] h-[120px] overflow-hidden'
      >
        <img
          src={previewUrl}
          alt='이미지 미리보기'
          className='text-[0px] w-full h-full object-cover'
        />
      </div>
      <Button
        styleType='primary'
        sizeType='full'
        onClick={handleInputClick}
      >
        {buttonLabel}
        <input
          ref={inputRef}
          type='file'
          accept='image/*'
          hidden
          onChange={handleImgInput}
        />
      </Button>
      <span>{value?.name}</span>
    </div>
  );
};

export default ImageInput;
