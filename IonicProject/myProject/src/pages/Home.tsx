import { IonButton, IonContent, IonHeader, IonIcon, IonImg, IonInput, IonItem, IonLabel, IonList, IonPage, IonRadio, IonRadioGroup, IonRouterLink, IonThumbnail, IonTitle, IonToolbar, useIonActionSheet } from '@ionic/react';
import { trash } from 'ionicons/icons';
import { useEffect, useState } from 'react';
import { getAllTrip, insertTrip, clearDB } from '../databaseHandler';
import { trip } from '../models/Trip';
import './Home.css';
import { deleteTripById } from '../databaseHandler';

const Home = () => {

  const [name, setName] = useState('')
  const [expences, setExpences] = useState('')
  const [picture, setPicture] = useState('')
  const [destination, setDestination] = useState('')
  const [description, setDescription] = useState('')
  const [date, setDate] = useState('')
  const [risk, setRisk] = useState(true)
  const [allTrips, setAllTrips] = useState<trip[]>([]);
  const [present] = useIonActionSheet();
  const [isDeleted, setIsDeleted] = useState(false)

  const [error, setError] = useState<any>({
    name : '',
    expences : '',
    picture : '',
    destination : '',
    date : ''
 })

  const fetchDataFromDB = async () => {
    const allTrips = await getAllTrip()
    setAllTrips(allTrips)
  }

  const saveHandler = async () => {
    if(validate()){
      const newTrip: trip = { 'name': name, 'expences': expences, 'picture': picture, 'destination': destination, 'description': description, 'date': date, 'risk': risk }
      await insertTrip(newTrip)
      fetchDataFromDB()
    }
  }
 
  const deletetrip = async (id:any) =>{
    let deleted = await deleteTripById(id);
    fetchDataFromDB()
  }

  const deleteDB = async () =>{
    clearDB()
    fetchDataFromDB()
  }
  useEffect(() => {
    fetchDataFromDB()
  }, [])


  const validate = () => {
   let ispass : boolean = true
   let note: any = {
      name : '',
      expences : '',
      picture : '',
      destination : '',
      date : ''
   }
    if (!name) {
      ispass = false
      note.name = 'Name can not be null'
    }
    if(!expences){
      ispass = false
      note.expences = 'expences can not be null'
    }
    if(!picture){
      ispass = false
      note.picture = 'picture can not be null'
    }
    if(!destination){
      ispass = false
      note.destination = 'destination can not be null'
    }
    if(!date){
      ispass = false
      note.date = 'date can not be null'
    }
    setError(note);
    return ispass;
  }

  return (
    <IonPage>
      <IonHeader>
        <IonToolbar color="warning">
          <IonTitle>Manage Trips</IonTitle>
          <IonButton  onClick ={deleteDB} color="danger" slot="end">
            <IonIcon slot="icon-only" icon={trash} ></IonIcon>
          </IonButton>
        </IonToolbar>
      </IonHeader>
      <IonContent>
        <IonItem>
          <IonLabel position='floating'>Name</IonLabel>
          <IonInput onIonChange={e => setName(e.detail.value!)}></IonInput>
          <div style={{color: 'red'}}>{error.name}</div>
        </IonItem>
        <IonItem>
          <IonLabel position='floating'>Destination</IonLabel>
          <IonInput onIonChange={e => setDestination(e.detail.value!)}></IonInput>
          <div style={{color: 'red'}}>{error.destination}</div>
        </IonItem>
        <IonItem>
          <IonLabel position='floating'>Description</IonLabel>
          <IonInput onIonChange={e => setDescription(e.detail.value!)}></IonInput>
        </IonItem>
        <IonItem>
          <IonLabel position='floating'>Expences</IonLabel>
          <IonInput onIonChange={e => setExpences(e.detail.value!)}></IonInput>
          <div style={{color: 'red'}}>{error.expences}</div>
        </IonItem> 

        <IonItem><IonLabel >Date</IonLabel></IonItem>
        <IonItem>
          <IonInput style={{ margin: "25px", height: "5px" }} type='date' onIonChange={e => setDate(e.detail.value!)}></IonInput>
          <div style={{color: 'red'}}>{error.date}</div>
        </IonItem> 

        <IonItem>
          <IonLabel position='floating'>Picture</IonLabel>
          <IonInput onIonChange={e => setPicture(e.detail.value!)}></IonInput>
          <div style={{color: 'red'}}>{error.picture}</div>
        </IonItem>

        <IonList>
          <IonItem><IonLabel>Require Risk</IonLabel></IonItem>
          <IonRadioGroup value={risk} onIonChange = {(e) => setRisk(e.detail.value)}>
            <IonItem>
              <IonLabel style={{ margin: "30px", height: "15px" }}>Yes</IonLabel>
              <IonRadio slot="end" value={true}></IonRadio>
            </IonItem>
            <IonItem>
              <IonLabel style={{ margin: "30px", height: "15px" }}>No</IonLabel>
              <IonRadio slot="end" value={false}></IonRadio>
            </IonItem>
          </IonRadioGroup>
        </IonList>

        <IonButton onClick={saveHandler} expand='block' class='ion-margin'>Save</IonButton>


        <IonList>
          {allTrips.map(c =>
            <IonItem key={c.id}>
              <IonThumbnail slot='start'>
                <IonImg src={c.picture}></IonImg>
              </IonThumbnail>
              <IonLabel>
                <IonRouterLink routerLink={'/Detail/' + c.id}>{c.name}</IonRouterLink>
                <IonLabel>{c.date}</IonLabel>
                
          <IonButton color="danger" slot="end" onClick={() =>
                                    present({
                                        header: 'Delete Trip?',
                                        buttons: [
                                            {
                                                text: 'Delete',
                                                role: 'destructive',
                                                handler() {
                                                    deletetrip(c.id);
                                                },
                                            },
                                            {
                                                text: 'Cancel',
                                                role: 'cancel',
                                                data: {
                                                    action: 'cancel',
                                                },
                                            },
                                        ],
                                        // onDidDismiss:,
                                    })
                                }>
            <IonIcon slot="icon-only" icon={trash} ></IonIcon>
          </IonButton>
              </IonLabel>
            </IonItem>
          )}
        </IonList>


      </IonContent>
    </IonPage>
  );
};

export default Home;
