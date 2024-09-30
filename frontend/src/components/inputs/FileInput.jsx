const FileInput = ({
  label,
  name,
  value,
  setValue,
  acceptType,
  containerStyle = 'grid grid-cols-[1fr_2fr]',
  ...props
}) => {
  const handleFileChange = (e) => {
    setValue({ ...value, [e.target.name]: e.target.files[0] });
  };

  return (
    <div className={containerStyle}>
      {label ? <span className='font-bold'>{label}</span> : null}
      <input
        type='file'
        name={name}
        accept={acceptType}
        onChange={handleFileChange}
        {...props}
      />
    </div>
  );
};

export default FileInput;
