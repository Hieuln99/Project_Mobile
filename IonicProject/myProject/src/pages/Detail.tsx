import { IonBackButton, IonButton, IonButtons, IonContent, IonHeader, IonIcon, IonImg, IonInput, IonItem, IonLabel, IonList, IonPage, IonRouterLink, IonSelect, IonSelectOption, IonThumbnail, IonTitle, IonToolbar, useIonActionSheet } from '@ionic/react';
import { useEffect, useState } from 'react';
import { useHistory, useParams } from 'react-router-dom';
import {  getTripById } from '../databaseHandler';
import { trip } from '../models/Trip';
import './Home.css';

import {reload, trash, triangleSharp} from 'ionicons/icons'

interface IdParam {
  id: string
}

const Detail: React.FC = () => {
  const [name, setName] = useState('')
  const [expences, setExpences] = useState('')
  const [picture, setPicture] = useState('')
  const [destination, setDestination] = useState('')
  const [description, setDescription] = useState('')
  const [date, setDate] = useState('')
  const [risk, setRisk] = useState(true)
  const { id } = useParams<IdParam>()


  const fetchDataFromDB = async()=>{
    const trip = await getTripById(Number.parseInt(id)) as trip
    setName(trip?.name)
    setExpences(trip?.expences)
    setPicture(trip?.picture)
    setDestination(trip?.destination)
    setDescription(trip?.description)
    setDate(trip?.date)
    setRisk(trip?.risk)
  }

  useEffect(()=>{
    fetchDataFromDB()
  })



  return (
    <IonPage>
      <IonHeader>
        <IonToolbar color="warning">
        <IonButtons slot="start">
            <IonBackButton />
          </IonButtons>
          <IonTitle>Trip Details</IonTitle>
        </IonToolbar>
      </IonHeader>
      <IonContent>
      <IonItem>
          <IonLabel position="floating">Name</IonLabel>
          <IonInput value={name} disabled></IonInput>
        </IonItem>
        <IonItem>
          <IonLabel position="floating">Destination</IonLabel>
          <IonInput value={destination} disabled></IonInput>
        </IonItem>
        <IonItem>
          <IonLabel position="floating">Description</IonLabel>
          <IonInput value={description} disabled></IonInput>
        </IonItem>
        <IonItem>
          <IonLabel position="floating">Expences</IonLabel>
          <IonInput value={expences} disabled></IonInput>
        </IonItem>
        <IonItem>
          <IonLabel position="floating">Date</IonLabel>
          <IonInput value={date} disabled></IonInput>
        </IonItem>
        <IonItem>
          <IonLabel position="floating">Risk</IonLabel>
          <IonInput value={risk ? "Yes" : "No"} disabled></IonInput>
        </IonItem>
        <IonItem>
          <IonLabel>Picture</IonLabel></IonItem>
          <IonThumbnail>
            <IonImg style={{height: "400px", width: "300px", margin: "150%"}}  src={picture} />
          </IonThumbnail>
        
      </IonContent>
    </IonPage>
  );
};
export default Detail;
