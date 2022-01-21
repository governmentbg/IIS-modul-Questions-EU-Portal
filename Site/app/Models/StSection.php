<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $StS_id
 * @property int        $StS_order
 * @property string     $StS_name
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class StSection extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'St_Section';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'StS_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'StS_id', 'StS_order', 'StS_name', 'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = [];

    /**
     * The attributes that should be casted to native types.
     *
     * @var array
     */
    protected $casts = [
        'StS_id' => 'int', 'StS_order' => 'int', 'StS_name' => 'string', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * Indicates if the model should be timestamped.
     *
     * @var boolean
     */
    public $timestamps = false;

    public static function boot()
    {
        parent::boot();

        static::creating(function ($article) {
            // $article->X_User_id = auth()->user()->id;
            $article->created_at = now();
            $article->updated_at = now();
        });

        static::saving(function ($article) {
            // $article->X_User_id = auth()->user()->id;
            $article->updated_at = now();
        });
    }

    // Scopes...

    // Functions ...

    // Relations ...

    public function eq_post()
    {
        return $this->hasMany(StPosts::class, 'StS_id');
    }
}
