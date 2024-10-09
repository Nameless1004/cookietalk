import FormInput from '../components/inputs/FormInput.jsx';
import { Suspense, useEffect, useState } from 'react';
import FileInput from '../components/inputs/FileInput.jsx';
import CookieCategorySelect from '../components/postCookie/CookieCategorySelect.jsx';
import SeriesModal from '../components/modals/SeriesModal.jsx';
import Button from '../components/inputs/Button.jsx';
import { X } from 'lucide-react';
import { cookiePostValidate } from '../utilities/validate.js';
import { usePostCookie } from '../query/cookieQuery.js';
import { useNavigate } from 'react-router-dom';

const INPUT_CONTAINER_LAYOUT = 'grid grid-cols-[100px_500px]';

const PostCookie = () => {
  const [postValues, setPostValues] = useState({
    title: '',
    thumbnail: null,
    video: null,
    series: null,
    description: '',
    category: null,
    attachment: null,
  });

  const [showSeriesModal, setShowSeriesModal] = useState(false);
  const { mutate, error, isError, isPending } = usePostCookie();

  useEffect(() => {
    if (isError) {
      alert(`업로드 요청 중 에러가 발생했습니다. : ${error}`);
    }
  }, [isError]);

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    const { isValid, message } = cookiePostValidate(postValues);

    if (!isValid) {
      alert(message);
      return;
    }

    mutate(postValues, {
      onSuccess: (response) => {
        alert('업로드가 시작되었습니다');
        navigate(`/cookie/${response.data.cookieId}`);
      },
    });
  };

  return (
    <div className='relative flex justify-center w-full h-full'>
      <form
        className='flex flex-col items-start w-[700px] gap-y-5 mt-10'
        onSubmit={handleSubmit}
      >
        <FormInput
          label='쿠키 제목 *'
          placeholder='제목을 입력해주세요.'
          name='title'
          containerStyle={INPUT_CONTAINER_LAYOUT}
          formValue={postValues}
          setFormValue={setPostValues}
        />
        <FileInput
          label='썸네일 *'
          acceptType='image/*'
          name='thumbnail'
          containerStyle={INPUT_CONTAINER_LAYOUT}
          value={postValues}
          setValue={setPostValues}
        />
        <FileInput
          label='영상 첨부 *'
          acceptType='video/mp4'
          name='video'
          containerStyle={INPUT_CONTAINER_LAYOUT}
          value={postValues}
          setValue={setPostValues}
        />
        <FileInput
          label='첨부파일'
          acceptType='.zip, .pdf'
          name='attachment'
          containerStyle={INPUT_CONTAINER_LAYOUT}
          value={postValues}
          setValue={setPostValues}
        />
        <CookieCategorySelect
          label='카테고리 *'
          formValue={postValues}
          setFormValue={setPostValues}
        />
        <div className={INPUT_CONTAINER_LAYOUT}>
          <span className='font-bold'>시리즈</span>
          {postValues.series ? (
            <div className='flex items-center gap-3'>
              <span>{postValues.series.title}</span>
              <X
                onClick={() => {
                  setPostValues({ ...postValues, series: null });
                }}
                size='16px'
              />
              <Button
                styleType='sub'
                onClick={() => {
                  setShowSeriesModal(true);
                }}
                className='rounded py-1 px-2'
              >
                변경하기
              </Button>
            </div>
          ) : (
            <div>
              <Button
                type='button'
                styleType='sub'
                className='rounded py-1 px-2 hover:shadow hover:-shadow--secondary-orange'
                onClick={() => {
                  setShowSeriesModal(true);
                }}
              >
                시리즈에 추가하기
              </Button>
            </div>
          )}
        </div>
        <FormInput
          label='상세 설명'
          type='textarea'
          name='description'
          formValue={postValues}
          setFormValue={setPostValues}
          placeholder='상세 설명을 입력해주세요.'
          containerStyle={INPUT_CONTAINER_LAYOUT}
        />
        <Button
          type='submit'
          styleType='primary'
          className='absolute bottom-10 right-10 font-bold rounded px-3 py-1 w-[100px]'
          disabled={isPending || isError}
        >
          {isPending ? '처리중' : '게시하기'}
        </Button>
      </form>
      {showSeriesModal ? (
        <Suspense fallback={null}>
          <SeriesModal
            setShowSeriesModal={setShowSeriesModal}
            formValue={postValues}
            setFormValue={setPostValues}
          />
        </Suspense>
      ) : null}
    </div>
  );
};

export default PostCookie;
