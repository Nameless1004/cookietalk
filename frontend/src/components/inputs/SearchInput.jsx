import TextInput from './TextInput.jsx';
import { useState } from 'react';
import { Search, X } from 'lucide-react';

// TODO: 검색 API랑 연결
const SearchInput = () => {
  const [searchOpen, setSearchOpen] = useState(false);

  const handleSearchBarToggle = () => {
    setSearchOpen(!searchOpen);
  };

  return (
    <>
      {searchOpen ? (
        <div className='relative'>
          <TextInput
            placeholder='검색할 키워드를 입력해주세요!'
            onBlur={handleSearchBarToggle}
            className='w-[300px] h-[30px] pl-3 -bg--light-gray-1 rounded-r-2xl rounded-l-2xl'
          />
          <X
            color='#2c2c2c'
            strokeWidth='1.5px'
            className='absolute top-1/2 right-3 transform -translate-y-1/2'
          />
        </div>
      ) : (
        <Search onClick={handleSearchBarToggle} />
      )}
    </>
  );
};

export default SearchInput;
