const FileInput = ({ label, setValue, acceptType, ...props }) => {
  return (
    <div className='flex gap-5'>
      {label ? <span className='font-bold'>{label}</span> : null}
      <input
        type='file'
        accept={acceptType}
        onChange={(e) => {
          setValue(e.target.files[0]);
        }}
        {...props}
      />
    </div>
  );
};

export default FileInput;
