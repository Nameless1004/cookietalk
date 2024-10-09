import { useEffect, useRef, useState } from 'react';
import TextInput from '../inputs/TextInput.jsx';
import { newSeiresValidate } from '../../utilities/validate.js';
import Button from '../inputs/Button.jsx';
import { useGetUserSeries, usePostSeries } from '../../query/seriesQuery.js';
import useUserStore from '../../zustand/userStore.js';

const SeriesModal = ({ setShowSeriesModal, formValue, setFormValue }) => {
  const [seriesList, setSeriesList] = useState([]);
  const [newSeries, setNewSeries] = useState('');
  const [selectedSeries, setSelectedSeries] = useState(null);
  const seriesListRef = useRef(null);
  const { id } = useUserStore((state) => state.user);
  const {
    data,
    isError: isGetSeriesError,
    error: getSeriesError,
    isSuccess: isGetSeriesSuccess,
  } = useGetUserSeries(id);
  const { mutate, isError: isPostSeriesError, error: postSeriesError } = usePostSeries();

  useEffect(() => {
    if (isGetSeriesSuccess) {
      setSeriesList([...data]);
    }

    if (isGetSeriesError) {
      alert('Get Series Error: ', getSeriesError);
    }
  }, [isGetSeriesSuccess, isGetSeriesError]);

  useEffect(() => {
    if (formValue.series) {
      setSelectedSeries({ id: formValue.series.id, title: formValue.series.title });
    }
  }, []);

  useEffect(() => {
    if (seriesListRef.current) {
      seriesListRef.current.scrollTop = seriesListRef.current.scrollHeight;
    }
  }, [seriesList]);

  const handleCloseModal = (e) => {
    if (e.target !== e.currentTarget) return;
    setShowSeriesModal(false);
  };

  const handleAddNewSeries = (e) => {
    e.preventDefault();

    const { isValid, message } = newSeiresValidate(newSeries, seriesList);
    if (!isValid) {
      alert(message);
      return;
    }

    mutate(newSeries, {
      onSuccess: (data) => {
        setSeriesList([...seriesList, data]);
        handleSelectSeries(data);
      },
    });
  };

  const handleSelectSeries = (currentSeries) => {
    setSelectedSeries(null);

    if (selectedSeries?.id !== currentSeries.id) {
      setSelectedSeries({ ...currentSeries });
    }
  };

  const handleSubmitSeries = () => {
    setFormValue({ ...formValue, series: selectedSeries });
    setShowSeriesModal(false);
  };

  return (
    <div
      onClick={handleCloseModal}
      id='modalBackground'
      className='flex justify-center items-center -bg--dark-gray-1/50 fixed top-0 left-0 w-full h-full'
    >
      <div className='relative flex flex-col items-center gap-5 w-[600px] h-[700px] p-10 bg-white ht'>
        <h3 className='font-bold'>시리즈에 추가하기</h3>
        <form
          onSubmit={handleAddNewSeries}
          className='flex gap-5 w-full'
        >
          <TextInput
            type='text'
            placeholder='새로운 시리즈 생성하기'
            className='w-[75%] pl-2 py-1 border border-gray-500 rounded'
            value={newSeries}
            setValue={setNewSeries}
          />
          <button className='p-2 rounded -bg--primary-orange text-white font-bold w-[20%] px-3'>생성하기</button>
        </form>
        <ul
          ref={seriesListRef}
          className='w-full max-h-[450px] overflow-y-auto'
        >
          {seriesList?.map((series, index) => {
            return (
              <li
                key={index}
                onClick={() => {
                  handleSelectSeries(series);
                }}
                className={`flex justify-start items-center px-3 border -border--light-gray-1 h-[40px] hover:cursor-pointer ${selectedSeries?.id === series.id ? '-bg--secondary-orange' : 'bg-white'}`}
              >
                {series.title}
              </li>
            );
          })}
        </ul>
        <div className='absolute bottom-10 right-10 flex justify-end gap-2 w-full'>
          <Button
            onClick={handleCloseModal}
            styleType='sub'
            className='font-bold rounded w-[60px] py-1'
          >
            취소
          </Button>
          <Button
            onClick={handleSubmitSeries}
            styleType='primary'
            className='w-[100px] rounded font-bold'
          >
            추가하기
          </Button>
        </div>
      </div>
    </div>
  );
};

export default SeriesModal;
