package com.example.platformermarc;

class InanimateBlockUpdateComponent implements UpdateComponent {

    private boolean mColliderNotSet = true;

    @Override
    public void update(long fps, Transform t, Transform playerTransform) {

        //An alternative would be to update the collider just once when it spawns.
        //But this would require spawn components - More code but a bit faster

        //update wird jeden Frame ausgefuehrt; um CPU Zyklen zu sparen, wird hier
        //stat spawn mit einem boolschen Wert geprueft, ob der Collider existiert

        if (mColliderNotSet){

            //only need to set the collider once because it will never move
            t.updateCollider();
            mColliderNotSet = false;
        }

    }

}
