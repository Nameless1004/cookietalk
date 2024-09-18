import FormInput from '../components/inputs/FormInput.jsx';
import { useState } from 'react';
import FileInput from '../components/inputs/FileInput.jsx';
import CookieCategorySelect from '../components/inputs/CookieCategorySelect.jsx';

const PostCookie = () => {
  const [postValues, setPostValues] = useState({
    title: '',
    video: null,
    series: null,
    description: '',
    category: null,
    lectureResources: null,
  });

  const handleVideoChange = (file) => {
    setPostValues({ ...postValues, video: file });
    // TODO: 비동기로 서버에 먼저 업로드
  };

  const handleLectureResourcesChange = (file) => {
    setPostValues({ ...postValues, lectureResources: file });
    // TODO: 비동기로 서버에 먼저 업로드
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log(postValues);
  };

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <FormInput
          label='쿠키 제목 *'
          placeholder='제목을 입력해주세요.'
          name='title'
          formValue={postValues}
          setFormValue={setPostValues}
        />
        <FileInput
          label='영상 첨부 *'
          acceptType='video/mp4'
          setValue={handleVideoChange}
        />
        <FileInput
          label='첨부파일'
          acceptType='.zip, .pdf'
          setValue={handleLectureResourcesChange}
        />
        <CookieCategorySelect
          label='카테고리 *'
          formValue={postValues}
          setFormValue={setPostValues}
        />
        {/*TODO: 시리즈 인풋 추가*/}
        <FormInput
          label='상세 설명'
          type='textarea'
          name='description'
          formValue={postValues}
          setFormValue={setPostValues}
          placeholder='상세 설명을 입력해주세요.'
        />
        <button
          type='submit'
          className='rounded bg-black text-white px-3 py-1'
        >
          등록하기
        </button>
      </form>
    </div>
  );
};

export default PostCookie;
