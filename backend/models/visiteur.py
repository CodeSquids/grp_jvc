from database import Database

class VisiteurModel:
    def __init__(self):
        self.db = Database()
    
    def get_all(self):
        cursor = self.db.get_cursor()
        cursor.execute("SELECT * FROM Visiteur ORDER BY n_visiteur")
        result = cursor.fetchall()
        cursor.close()
        return result
    
    def get_by_id(self, n_visiteur):
        cursor = self.db.get_cursor()
        cursor.execute("SELECT * FROM Visiteur WHERE n_visiteur = %s", (n_visiteur,))
        result = cursor.fetchone()
        cursor.close()
        return result
    
    def get_by_nom(self, nom):
        cursor = self.db.get_cursor()
        cursor.execute("SELECT * FROM Visiteur WHERE nom LIKE %s", (f'%{nom}%',))
        result = cursor.fetchall()
        cursor.close()
        return result
    
    def create(self, n_visiteur, nom, adresse):
        cursor = self.db.get_cursor()
        try:
            cursor.execute(
                "INSERT INTO Visiteur (n_visiteur, nom, adresse) VALUES (%s, %s, %s)",
                (n_visiteur, nom, adresse)
            )
            self.db.commit()
            return {'success': True, 'message': 'Visiteur créé avec succès'}
        except Exception as e:
            return {'success': False, 'message': str(e)}
        finally:
            cursor.close()
    
    def update(self, n_visiteur, nom, adresse):
        cursor = self.db.get_cursor()
        try:
            cursor.execute(
                "UPDATE Visiteur SET nom = %s, adresse = %s WHERE n_visiteur = %s",
                (nom, adresse, n_visiteur)
            )
            self.db.commit()
            return {'success': True, 'message': 'Visiteur mis à jour avec succès'}
        except Exception as e:
            return {'success': False, 'message': str(e)}
        finally:
            cursor.close()
    
    def delete(self, n_visiteur):
        cursor = self.db.get_cursor()
        try:
            # Vérifier si le visiteur a des visites
            cursor.execute("SELECT COUNT(*) as count FROM Visiter WHERE n_visiteur = %s", (n_visiteur,))
            result = cursor.fetchone()
            if result['count'] > 0:
                return {'success': False, 'message': 'Impossible de supprimer: ce visiteur a des visites enregistrées'}
            
            cursor.execute("DELETE FROM Visiteur WHERE n_visiteur = %s", (n_visiteur,))
            self.db.commit()
            return {'success': True, 'message': 'Visiteur supprimé avec succès'}
        except Exception as e:
            return {'success': False, 'message': str(e)}
        finally:
            cursor.close()
    
    def search(self, critere, valeur):
        """Recherche par numéro ou nom"""
        cursor = self.db.get_cursor()
        if critere == 'numero':
            cursor.execute("SELECT * FROM Visiteur WHERE n_visiteur = %s", (valeur,))
            result = cursor.fetchall()
        else:  # nom
            cursor.execute("SELECT * FROM Visiteur WHERE nom LIKE %s", (f'%{valeur}%',))
            result = cursor.fetchall()
        cursor.close()
        return result