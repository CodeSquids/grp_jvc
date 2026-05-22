from database import Database

class VisiterModel:
    def __init__(self):
        self.db = Database()
    
    def get_all(self):
        cursor = self.db.get_cursor()
        cursor.execute("SELECT * FROM Visiter ORDER BY n_visiter")
        result = cursor.fetchall()
        cursor.close()
        return result
    
    def create(self, n_visiter, n_visiteur, n_site, nbjours, date_visite):
        cursor = self.db.get_cursor()
        try:
            cursor.execute(
                "INSERT INTO Visiter (n_visiter, n_visiteur, n_site, nbjours, date_visite) VALUES (%s, %s, %s, %s, %s)",
                (n_visiter, n_visiteur, n_site, nbjours, date_visite)
            )
            self.db.commit()
            return {'success': True, 'message': 'Visite créé avec succès'}
        except Exception as e:
            return {'success': False, 'message': str(e)}
        finally:
            cursor.close()
    
    def update(self, n_visiter, n_visiteur, n_site, nbjours, date_visite):
        cursor = self.db.get_cursor()
        try:
            cursor.execute(
                "UPDATE Visiter SET n_visiteur = %s, n_site = %s, nbjours= %s, date_visite= %s, WHERE n_visiter = %s",
                ( n_visiteur, n_site, nbjours, date_visite ,n_visiter)
            )
            self.db.commit()
            return {'success': True, 'message': 'Visite mis à jour avec succès'}
        except Exception as e:
            return {'success': False, 'message': str(e)}
        finally:
            cursor.close()
    
    def delete(self, n_visiter):
        cursor = self.db.get_cursor()
        try:
            # Vérifier si le visiter a des visites
            cursor.execute("SELECT COUNT(*) as count FROM Visiter WHERE n_visiter = %s", (n_visiter,))
            result = cursor.fetchone()
            if result['count'] > 0:
                return {'success': False, 'message': 'Impossible de supprimer: ce visiter a des visites enregistrées'}
            
            cursor.execute("DELETE FROM Visiter WHERE n_visiter = %s", (n_visiter,))
            self.db.commit()
            return {'success': True, 'message': 'Visiter supprimé avec succès'}
        except Exception as e:
            return {'success': False, 'message': str(e)}
        finally:
            cursor.close()
    
    