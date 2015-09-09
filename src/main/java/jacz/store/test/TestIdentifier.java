package jacz.store.test;

import jacz.store.common.IdentifierFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Alberto
 * Date: 3/06/14
 * Time: 19:58
 * To change this template use OldFile | Settings | OldFile Templates.
 */
public class TestIdentifier {

    public static void main(String[] args) {
        IdentifierFactory identifierFactory = new IdentifierFactory();

        for (int i = 0; i < 64*64 + 5; i++) {
            System.out.println(identifierFactory.getOneIdentifier());
        }
    }
}
