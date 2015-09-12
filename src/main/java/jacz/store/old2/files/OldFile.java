package jacz.store.old2.files;

/**
 * A file located in the disk
 * todo remove
 */
public abstract class OldFile {

//    public static final String FILE_HASH = "fileHash";
//
//    public static final String FILE_NAME = "fileName";
//
//    public static final String FILE_LENGTH = "fileLength";
//
//    public static final String COMMENT = "comment";
//
//    /**
//     * hash of the file (serves as key in a corresponding FileHashDatabase)
//     */
//    private String fileHash;
//
//    /**
//     * Name of the original file (not for accessing, but for metadata)
//     */
//    private String fileName;
//
//    /**
//     * Length of the file (in bytes)
//     */
//    private Long fileLength;
//
//    /**
//     * e.g. "recorded at Woodstock '83", "director's cut"
//     */
//    private String comment;
//
////    public OldFile(String container, Database database) throws DBException, IOException {
////        super(container, database);
////    }
////
////    public OldFile(String container, Database database, String identifier) throws DBException, IOException {
////        super(container, database, identifier);
////    }
//
//    protected OldFile(String container, ItemInterface itemInterface, Map<String, String> values, boolean itemIsNew) throws ParseException {
//        super(container, itemInterface, values, itemIsNew);
////        fileHash = initializeValueString(FILE_HASH, values);
////        fileName = initializeValueString(FILE_NAME, values);
////        fileLength = initializeValueLong(FILE_LENGTH, values);
////        comment = initializeValueString(COMMENT, values);
//    }
//
//    public List<String> getFields() {
//        List<String> fields = super.getFields();
//        fields.add(FILE_HASH);
//        fields.add(FILE_NAME);
//        fields.add(FILE_LENGTH);
//        fields.add(COMMENT);
//        return fields;
//    }
//
//    @Override
//    protected void initValues() {
//        fileHash = null;
//        fileName = null;
//        fileLength = null;
//        comment = null;
//    }
//
//    @Override
//    protected void loadValues(Map<String, String> values) throws ParseException {
//        fileHash = initializeValueString(FILE_HASH, values);
//        fileName = initializeValueString(FILE_NAME, values);
//        fileLength = initializeValueLong(FILE_LENGTH, values);
//        comment = initializeValueString(COMMENT, values);
//    }
//
//    public String getFileHash() throws DBException, ParseException {
//        return fileHash;
//    }
//
//    public void setFileHash(String fileHash) throws DBException, IOException {
//        this.fileHash = updateField(this.fileHash, fileHash, "");
//    }
//
//    public String getFileName() {
//        return fileName;
//    }
//
//    public void setFileName(String fileName) throws DBException, IOException {
//        this.fileName = updateField(this.fileName, fileName, "fileName");
//    }
//
//    public Long getFileLength() throws DBException, ParseException {
//        return fileLength;
//    }
//
//    public void setFileLength(Long fileLength) throws DBException, IOException {
//        this.fileLength = updateField(this.fileLength, fileLength, "fileLength");
//    }
//
//    public String getComment() throws DBException, ParseException {
//        return comment;
//    }
//
//    public void setComment(String comment) throws DBException, IOException {
//        this.comment = updateField(this.comment, comment, "comment");
//    }
//
//    @Override
//    public Float match(LibraryItem anotherItem) {
//        if (!(anotherItem instanceof OldFile)) {
//            return -1f;
//        }
//        OldFile anotherOldFile = (OldFile) anotherItem;
//        // the hash indicates if the files are actually the same
//        return fileHash.equals(anotherOldFile.fileHash) ? 1f : -1f;
//    }
//
//    @Override
//    public boolean mergeData(LibraryItem anotherItem, IdentifierMap identifierMap) throws IllegalDataException, DBException, ParseException {
//        return false;  //To change body of implemented methods use OldFile | Settings | OldFile Templates.
//    }
//
//    @Override
//    public boolean mergeCrossReferencesAux(LibraryItem anotherItem, IdentifierMap identifierMap) throws IllegalDataException, DBException, ParseException {
//        return false;  //To change body of implemented methods use OldFile | Settings | OldFile Templates.
//    }
//
//    @Override
//    public void merge(LibraryItem anotherItem) {
//        super.merge(anotherItem);    //To change body of overridden methods use OldFile | Settings | OldFile Templates.
//    }
}
