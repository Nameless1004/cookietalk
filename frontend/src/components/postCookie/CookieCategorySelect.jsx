import SelectButton from '../inputs/SelectButton.jsx';
import COOKIE_CATEGORIES from '../../constant/cookieCaterogies.js';

const CookieCategorySelect = ({ label, formValue, setFormValue }) => {
  const handleSelect = ({ id, name }) => {
    setFormValue({ ...formValue, category: { id, name } });
  };

  return (
    <div className='flex gap-5'>
      {label ? <span className='font-bold'>{label}</span> : null}
      <div className='flex gap-3'>
        {COOKIE_CATEGORIES.map((category, index) => {
          return (
            <SelectButton
              key={index}
              name={category.name}
              value={formValue.category}
              onClick={() => handleSelect(category)}
            >
              {category.label}
            </SelectButton>
          );
        })}
      </div>
    </div>
  );
};

export default CookieCategorySelect;
