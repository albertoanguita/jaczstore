package jacz.store.old2.files;

/**
 * A file that can be played/reproduced (e.g. an mp3 file, an avi file)
 */
public abstract class OldPlayFile {

//    public static final String REPRODUCTION_TIME = "reproductionTime";
//
//    public static final String SHORT_QUALITY = "shortQuality";
//
//    /**
//     * Reproduction time of the audio file (in milliseconds)
//     */
//    protected Long reproductionTime;
//
//    protected ShortQuality shortQuality;
//
////    public OldPlayFile(String container, Database database) throws DBException, IOException {
////        super(container, database);
////    }
////
////    public OldPlayFile(String container, Database database, String identifier) throws DBException, IOException {
////        super(container, database, identifier);
////    }
//
//    protected OldPlayFile(String container, ItemInterface itemInterface, Map<String, String> values, boolean itemIsNew) throws ParseException {
//        super(container, itemInterface, values, itemIsNew);
//        // TODO
//        reproductionTime = Long.parseLong(values.get("reproductionTime"));
//        shortQuality = ShortQuality.parse(values.get("shortQuality"));
//    }
//
//    public List<String> getFields() {
//        List<String> fields = super.getFields();
//        fields.add(REPRODUCTION_TIME);
//        fields.add(SHORT_QUALITY);
//        return fields;
//    }
//
//    @Override
//    protected void initValues() {
//        // TODO??
//        reproductionTime = null;
//        shortQuality = null;
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
}
