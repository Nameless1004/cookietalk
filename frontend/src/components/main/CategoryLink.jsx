import { Link } from 'react-router-dom';
import CategoryIcon from '../postCookie/CategoryIcon.jsx';
const CategoryLink = ({ category }) => {
  const { id, name, label } = category;
  return (
    <Link to={`category/${id}`}>
      <div className='flex flex-col justify-center gap-1 w-[100px] h-[100px] -bg--light-gray-1'>
        <CategoryIcon
          className='w-full'
          size='50px'
          strokeWidth='1px'
          category={name}
        />
        <p className='text-center'>{label}</p>
      </div>
    </Link>
  );
};

export default CategoryLink;
