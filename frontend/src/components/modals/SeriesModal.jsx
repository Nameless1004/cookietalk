import { useState } from 'react';
import TextInput from '../inputs/TextInput.jsx';
import { newSeiresValidate } from '../../utilities/validate.js';

const SeriesModal = ({ setShowSeriesModal }) => {
  const [seriesList, setSeriesList] = useState(['기존 시리즈', '기존 시리즈2']);
  const [newSeries, setNewSeries] = useState('');
  const [selectedSeries, setSelectedSeries] = useState(null);

  const handleShowSeriesModal = (e) => {
    if (e.target !== e.currentTarget) return;
    setShowSeriesModal(false);
  };

  const addNewSeries = (e) => {
    e.preventDefault();

    const { isValid, message } = newSeiresValidate(newSeries, seriesList);
    if (!isValid) {
      alert(message);
      return;
    }

    setSeriesList([...seriesList, newSeries]);
    selectSeries(newSeries);
  };

  const selectSeries = (currentSeries) => {
    setSelectedSeries(null);

    if (selectedSeries !== currentSeries) {
      setSelectedSeries(currentSeries);
    }
  };

  return (
    <div
      onClick={handleShowSeriesModal}
      id='modalBackground'
      className='flex justify-center items-center -bg--dark-gray-1/50 fixed top-0 left-0 w-full h-full'
    >
      <div className='flex flex-col items-center gap-5 w-[600px] h-[700px] p-10 bg-white ht'>
        <h3 className='font-bold'>시리즈에 추가하기</h3>
        <form
          onSubmit={addNewSeries}
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
        <ul className='w-full'>
          {seriesList.map((series, index) => {
            return (
              <li
                key={index}
                onClick={() => {
                  selectSeries(series);
                }}
                className={`flex justify-start items-center px-3 border -border--light-gray-1 h-[40px] ${selectedSeries === series ? 'bg-red-300' : 'bg-white'}`}
              >
                {series}
              </li>
            );
          })}
        </ul>
      </div>
    </div>
  );
};

export default SeriesModal;
